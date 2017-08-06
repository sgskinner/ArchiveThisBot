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
 * StashThisBot - Summon this bot to archive URLs in an archive service.
 * Copyright (C) 2017  S.G. Skinner
 */

package org.sgs.stashbot.service;


import org.sgs.stashbot.model.StashResult;
import org.sgs.stashbot.model.Postable;

import net.dean.jraw.models.Comment;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Message;
import net.dean.jraw.models.Submission;

public interface RedditService {
    Listing<Submission> getSubredditSubmissions(String subredditName);

    Submission getFullSubmissionData(Submission submission);

    void performAuth();

    boolean isAuthenticated();

    void postStashResult(StashResult stashResult);

    Listing<Message> getUnreadMessages();

    Comment getSummoningComment(Message message);

    Submission getSubmissionById(String submissionId);

    void markMessageRead(Message message);

    Postable getTargetPostable(Message message);
}