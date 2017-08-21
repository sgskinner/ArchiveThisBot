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

package org.sgs.stashbot;

import org.junit.Assert;
import org.junit.Test;
import org.sgs.stashbot.model.StashResult;
import org.sgs.stashbot.service.ArchiveService;
import org.sgs.stashbot.spring.SpringContext;

public class ArchiveIsServiceTest extends GeneratorTestBase {

    @Test
    public void testAppInit() {

        StashResult stashResult = generateDummyStashResult();

        ArchiveService archiveService = SpringContext.getBean(ArchiveService.class);
        Assert.assertTrue("ArchiveService could not initialize.", archiveService != null);
    }

}
