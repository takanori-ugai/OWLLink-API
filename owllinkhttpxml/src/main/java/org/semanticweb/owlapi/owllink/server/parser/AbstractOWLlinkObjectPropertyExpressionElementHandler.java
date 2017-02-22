/*
 * Copyright (C) 2010, Ulm University
 *
 * Modifications to the initial code base are copyright of their
 * respective authors, or their employers as appropriate.  Authorship
 * of the modifications may be determined from the ChangeLog placed at
 * the end of this file.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.semanticweb.owlapi.owllink.server.parser;

import org.coode.owlapi.owlxmlparser.AbstractOWLObjectPropertyElementHandler;
import org.coode.owlapi.owlxmlparser.OWLXMLParserException;
import org.coode.owlapi.owlxmlparser.OWLXMLParserHandler;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.owllink.KBRequest;

/**
 * Author: Olaf Noppens
 * Date: 28.11.2009
 */
public abstract class AbstractOWLlinkObjectPropertyExpressionElementHandler<R extends KBRequest> extends AbstractOWLlinkObjectRequestElementHandler<R, OWLObjectPropertyExpression> {

    public AbstractOWLlinkObjectPropertyExpressionElementHandler(OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    public void handleChild(AbstractOWLObjectPropertyElementHandler handler) throws OWLXMLParserException {
        super.o = handler.getOWLObject();
    }
}
