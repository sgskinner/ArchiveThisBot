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


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sgs.atbot.service.ArchiveIsService;
import org.sgs.atbot.service.RedditService;

public class ArchiveThisBot {
    private static final Logger LOG = LogManager.getLogger(ArchiveThisBot.class);

    private RedditService redditService;
    private ArchiveIsService archiveIsService;


    protected RedditService getRedditService() {
        return redditService;
    }


    public void setRedditService(RedditService redditService) {
        this.redditService = redditService;
    }


    protected ArchiveIsService getArchiveIsService() {
        return archiveIsService;
    }


    public void setArchiveIsService(ArchiveIsService archiveIsService) {
        this.archiveIsService = archiveIsService;
    }


    public void performAuth() {
        getRedditService().performAuth();
    }


    public boolean isAuthenticated() {
        return getRedditService().isAuthenticated();
    }


    public static void main(String... sgs) {
        LOG.debug("Starting main loop.");
    }
}
