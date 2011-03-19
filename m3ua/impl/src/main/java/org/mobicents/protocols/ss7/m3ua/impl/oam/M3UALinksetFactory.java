/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */ 
package org.mobicents.protocols.ss7.m3ua.impl.oam;

import org.mobicents.protocols.ss7.m3ua.impl.as.AsImpl;
import org.mobicents.protocols.ss7.m3ua.impl.as.RemSgpImpl;
import org.mobicents.ss7.linkset.oam.LinkOAMMessages;
import org.mobicents.ss7.linkset.oam.Linkset;
import org.mobicents.ss7.linkset.oam.LinksetFactory;

/**
 * 
 * @author amit bhayani
 * 
 */
public class M3UALinksetFactory extends LinksetFactory {

    private static final String NAME = "m3ua";

    private RemSgpImpl remSgp;

    public M3UALinksetFactory() {
        super();
    }

    @Override
    public String getName() {
        return NAME;
    }

    public RemSgpImpl getRemSgp() {
        return remSgp;
    }

    public void setRemSgp(RemSgpImpl remSgp) {
        this.remSgp = remSgp;
    }

    @Override
    public Linkset createLinkset(String[] options) throws Exception {
        // the expected command is "linkset create m3ua opc 1 apc 2 ni 3
        // as as1 linkset1". We know length is 14
        if (options.length != 12) {
            throw new Exception(LinkOAMMessages.INVALID_COMMAND);
        }

        String option = options[3];

        // If first option is not OPC
        if (option.compareTo("opc") != 0) {
            throw new Exception(LinkOAMMessages.INVALID_COMMAND);
        }

        int opc = Integer.parseInt(options[4]);

        if (options[5].compareTo("apc") != 0) {
            throw new Exception(LinkOAMMessages.INVALID_COMMAND);
        }

        int dpc = Integer.parseInt(options[6]);

        if (options[7].compareTo("ni") != 0) {
            throw new Exception(LinkOAMMessages.INVALID_COMMAND);
        }

        int ni = Integer.parseInt(options[8]);

        if (options[9].compareTo("as") != 0) {
            throw new Exception(LinkOAMMessages.INVALID_COMMAND);
        }

        String asName = options[10];

        if (asName == null) {
            throw new Exception(LinkOAMMessages.INVALID_COMMAND);
        }

        AsImpl asImpl = (AsImpl) this.remSgp.getAs(asName);

        if (asImpl == null) {
            throw new Exception(LinkOAMMessages.INVALID_COMMAND);
        }
        String name = options[11];

        if (name == null) {
            throw new Exception(LinkOAMMessages.INVALID_COMMAND);
        }

        return new M3UALinkset(name, opc, dpc, ni, asImpl);
    }

}