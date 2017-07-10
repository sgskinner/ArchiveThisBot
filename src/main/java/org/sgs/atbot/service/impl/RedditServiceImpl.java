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

package org.sgs.atbot.service.impl;

import org.sgs.atbot.service.AuthService;
import org.sgs.atbot.service.RedditService;

import net.dean.jraw.http.UserAgent;


public class RedditServiceImpl implements RedditService {

    private UserAgent userAgent;
    private AuthService authService;


    public RedditServiceImpl() {
        //
    }


    @Override
    public void performAuth() {
        //TODO: implement
    }


    @Override
    public boolean isAuthenticated() {
        //TODO: implement
        return false;
    }

}
