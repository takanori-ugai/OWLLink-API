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

package org.semanticweb.owlapi.owllink.builtin.requests;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.owllink.builtin.response.SetOfDataPropertySynsets;

/**
 * Represents a <a href="http://www.owllink.org/owllink-20091116/#IndividualDataPropQueries">GetDataPropertiesOfLiteral</a>
 * request in the OWLlink specification.
 * Author: Olaf Noppens
 * Date: 23.10.2009
 */
public class GetDataPropertiesOfLiteral extends AbstractKBRequestWithOneObjectAndBooleanValue<
        SetOfDataPropertySynsets,
        OWLLiteral> {

    public GetDataPropertiesOfLiteral(IRI iri, OWLLiteral object, boolean bool) {
        super(iri, object, bool);
    }

    public GetDataPropertiesOfLiteral(IRI iri, OWLLiteral object) {
        this(iri, object, false);
    }

    public boolean isNegative() {
        return super.bool;
    }

    public OWLLiteral getTargetValue() {
        return super.object;
    }

    public void accept(RequestVisitor visitor) {
        visitor.answer(this);
    }
}