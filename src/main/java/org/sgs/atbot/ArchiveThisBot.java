/*
 * GNU GENERAL PUBLIC LICENSE
 * Version 3, 29 June 2007
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * ArchiveThisBot - Summon this bot to archive Reddit URLs in archive.is
 * Copyright (C) 2016  S.G. Skinner
 */

package org.sgs.atbot;


import java.util.List;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sgs.atbot.model.ArchiveResult;
import org.sgs.atbot.service.ArchiveResultBoService;
import org.sgs.atbot.service.ArchiveService;
import org.sgs.atbot.service.RedditService;
import org.sgs.atbot.spring.SpringContext;
import org.sgs.atbot.url.UrlMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import net.dean.jraw.models.Comment;
import net.dean.jraw.models.CommentNode;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;

@Component
public class ArchiveThisBot {
    private static final Logger LOG = LogManager.getLogger(ArchiveThisBot.class);
    private static final long SLEEP_INTERVAL = 10 * 1000; // 10 seconds in millis
    private static final long OAUTH_REFRESH_INTERVAL = 50 * 60 * 1000; // 50 minutes in millis

    private final RedditService redditService;
    private final ArchiveService archiveIsService;
    private final List<String> subredditList;
    private final ArchiveResultBoService archiveResultBoService;


    @Autowired
    public ArchiveThisBot(RedditService redditService, ArchiveService archiveIsService, List<String> subredditList, ArchiveResultBoService archiveResultBoService) {
        this.redditService = redditService;
        this.archiveIsService = archiveIsService;
        this.subredditList = subredditList;
        this.archiveResultBoService = archiveResultBoService;
    }


    private void run() {

        // OAuth token is set to expire in 1 hour from authenticating, so we need to watch for refreshing
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        performAuth();
        if (!isAuthenticated()) {
            LOG.fatal("Could not authenticate, exiting!");
            return;
        }

        do {
            for (String subredditName : getSubredditList()) {
                Listing<Submission> submissions = getRedditService().getSubredditSubmissions(subredditName);
                for (Submission submission : submissions) {
                    if (submission == null || submission.getCommentCount() < 1 || submission.getId() == null) {
                        // GIGO, move on
                        LOG.warn("Bad submission, skipping: " + submission);
                        continue;
                    }
                    submission = getRedditService().getFullSubmissionData(submission);
                    CommentNode commentNode = submission.getComments();
                    recurseThroughComments(commentNode, submission);
                }
            }

            // OAuth token needs refreshing every 60 minutes, so we're going to refresh every 50
            if (stopWatch.getTotalTimeMillis() > OAUTH_REFRESH_INTERVAL) {
                stopWatch = new StopWatch();
                stopWatch.start();
                performAuth();
            }

            try {
                Thread.sleep(SLEEP_INTERVAL);
            } catch (InterruptedException e) {
                LOG.warn("Unexpectedly woken from sleep!: " + e.getMessage());
            }

        } while (isAuthenticated());
    }


    private void recurseThroughComments(CommentNode commentNode, Submission submission) {

        if (commentNode == null) {
            LOG.warn("No comments found for submission!: " + submission.getShortURL());
            return;
        }

        // Depth-first traversal, might want to also try breadth-first and assess which is better
        if (commentNode.getChildren().size() > 0) {
            for (CommentNode childNode : commentNode.getChildren()) {
                recurseThroughComments(childNode, submission);
            }
        }

        // If we're here, we're a leaf node, so do summons search here
        if (isCommentSummoning(commentNode) && !isAlreadyServiced(commentNode) && !isUserBlacklisted(commentNode.getComment().getAuthor())) {
            processSummons(commentNode);
        }

    }


    private boolean isUserBlacklisted(String authorUsername) {
        throw new NotImplementedException("TODO: Create blacklist objects and logic");
    }


    private boolean isAlreadyServiced(CommentNode summoningCommentNode) {
        return getArchiveResultBoService().existsByParentCommentId(summoningCommentNode.getParent().getComment().getId());
    }


    private boolean isCommentSummoning(CommentNode commentNode) {
        Comment comment = commentNode.getComment();
        String body = comment.getBody();

        if (StringUtils.isNotBlank(body)) {
            //TODO: Add matcher so that we can report the actual match
            if (body.contains("!ArchiveThis") || body.contains("!Archive This") || body.contains("Archive This!") || body.contains("Archive This!")) {
                LOG.debug("Found summon hit(Comment#getId()): " + comment.getId());
                return true;
            }
        }

        return false;
    }


    private void processSummons(CommentNode summoningCommentNode) {
        LOG.debug("Processing summons: " + summoningCommentNode.getComment().getId());

        // Pull all urls that we can find in the parent comment
        CommentNode parentCommentNode = summoningCommentNode.getParent();
        Comment parentComment = parentCommentNode.getComment();
        String body = parentComment.getBody();

        List<String> extractedUrls = UrlMatcher.extractUrls(body);
        if (extractedUrls.size() > 0) {
            ArchiveResult archivedResult = getArchiveService().archiveUrls(parentCommentNode, summoningCommentNode, extractedUrls);
            getRedditService().postArchiveResult(archivedResult);
        }
    }


    private RedditService getRedditService() {
        return redditService;
    }


    private ArchiveService getArchiveService() {
        return archiveIsService;
    }


    protected void performAuth() {
        getRedditService().performAuth();
    }


    protected boolean isAuthenticated() {
        return getRedditService().isAuthenticated();
    }


    private List<String> getSubredditList() {
        return subredditList;
    }


    public ArchiveResultBoService getArchiveResultBoService() {
        return archiveResultBoService;
    }


    public static void main(String... sgs) {
        LOG.info("Intializing bot...");
        ArchiveThisBot atbot = SpringContext.getBean(ArchiveThisBot.class);

        LOG.info("Intializing complete, starting main loop.");
        atbot.run();
    }

}
