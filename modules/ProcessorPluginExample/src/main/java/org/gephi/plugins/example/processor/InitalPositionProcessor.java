/*
Copyright 2008-2011 Gephi
Authors : Mathieu Bastian <mathieu.bastian@gephi.org>
Website : http://www.gephi.org

This file is part of Gephi.

DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.

Copyright 2011 Gephi Consortium. All rights reserved.

The contents of this file are subject to the terms of either the GNU
General Public License Version 3 only ("GPL") or the Common
Development and Distribution License("CDDL") (collectively, the
"License"). You may not use this file except in compliance with the
License. You can obtain a copy of the License at
http://gephi.org/about/legal/license-notice/
or /cddl-1.0.txt and /gpl-3.0.txt. See the License for the
specific language governing permissions and limitations under the
License.  When distributing the software, include this License Header
Notice in each file and include the License files at
/cddl-1.0.txt and /gpl-3.0.txt. If applicable, add the following below the
License Header, with the fields enclosed by brackets [] replaced by
your own identifying information:
"Portions Copyrighted [year] [name of copyright owner]"

If you wish your version of this file to be governed by only the CDDL
or only the GPL Version 3, indicate your decision by adding
"[Contributor] elects to include this software in this distribution
under the [CDDL or GPL Version 3] license." If you do not indicate a
single choice of license, a recipient has the option to distribute
your version of this file under either the CDDL, the GPL Version 3 or
to extend the choice of license to its licensees as provided above.
However, if you add GPL Version 3 code and therefore, elected the GPL
Version 3 license, then the option applies only if the new code is
made subject to such option by the copyright holder.

Contributor(s):

Portions Copyrighted 2011 Gephi Consortium.
 */
package org.gephi.plugins.example.processor;

import java.util.Random;
import org.gephi.io.importer.api.ContainerUnloader;
import org.gephi.io.importer.api.NodeDraft;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.io.processor.spi.Processor;
import org.openide.util.lookup.ServiceProvider;

/**
 * Example of a new processor that set up the initial position of nodes using a
 * hash so it's always the same for a given graph.
 * <p>
 * Processors are responsible for adding data imported in importers to the main
 * graph structure. The user can choose which processor to use from the Import
 * Report panel (Full graph, Append...). This example modifies the default
 * processor and set the nodes' position.
 *
 * @author Mathieu Bastian
 */
@ServiceProvider(service = Processor.class, position = 1000)
public class InitalPositionProcessor extends DefaultProcessor implements Processor {

    @Override
    public void process() {
        if (containers.length > 1) {
            throw new RuntimeException("This processor can only handle single containers");
        }
        ContainerUnloader container = containers[0];

        long hash = 0;

        //Calculate the hash of the current graph
        for (NodeDraft nodeDraft : container.getNodes()) {
            String id = nodeDraft.getId();
            hash += id.hashCode();
        }

        //Create a random with this seed
        Random random = new Random(hash);
        for (NodeDraft nodeDraft : container.getNodes()) {
            nodeDraft.setX((float) ((0.01 + random.nextDouble()) * 1000) - 500);
            nodeDraft.setY((float) ((0.01 + random.nextDouble()) * 1000) - 500);
        }

        //Call default processor
        super.process();
    }

    @Override
    public String getDisplayName() {
        return "Inital position";
    }
}
