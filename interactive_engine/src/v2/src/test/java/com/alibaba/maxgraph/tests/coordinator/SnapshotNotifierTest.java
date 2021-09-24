/**
 * Copyright 2020 Alibaba Group Holding Limited.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.maxgraph.tests.coordinator;

import com.alibaba.maxgraph.common.config.CommonConfig;
import com.alibaba.maxgraph.common.config.Configs;
import com.alibaba.maxgraph.groot.common.discovery.MaxGraphNode;
import com.alibaba.maxgraph.groot.common.discovery.NodeDiscovery;
import com.alibaba.maxgraph.common.RoleType;
import com.alibaba.maxgraph.groot.common.rpc.RoleClients;
import com.alibaba.maxgraph.groot.coordinator.FrontendSnapshotClient;
import com.alibaba.maxgraph.groot.coordinator.NotifyFrontendListener;
import com.alibaba.maxgraph.groot.coordinator.SnapshotManager;
import com.alibaba.maxgraph.groot.coordinator.SnapshotNotifier;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Collections;

import static org.mockito.Mockito.*;

public class SnapshotNotifierTest {

    @Test
    void testSnapshotNotifier() {
        NodeDiscovery discovery = mock(NodeDiscovery.class);
        SnapshotManager snapshotManager = mock(SnapshotManager.class);
        RoleClients<FrontendSnapshotClient> roleClients = mock(RoleClients.class);
        SnapshotNotifier snapshotNotifier = new SnapshotNotifier(discovery, snapshotManager, null, roleClients);
        snapshotNotifier.start();

        MaxGraphNode localNode = MaxGraphNode.createLocalNode(Configs.newBuilder()
                .put(CommonConfig.ROLE_NAME.getKey(), RoleType.COORDINATOR.getName()).build(), 1111);
        snapshotNotifier.nodesJoin(RoleType.FRONTEND, Collections.singletonMap(1, localNode));
        ArgumentCaptor<NotifyFrontendListener> captor = ArgumentCaptor.forClass(NotifyFrontendListener.class);
        verify(snapshotManager).addListener(captor.capture());
        NotifyFrontendListener listener = captor.getValue();
        snapshotNotifier.nodesLeft(RoleType.FRONTEND, Collections.singletonMap(1, localNode));
        verify(snapshotManager).removeListener(listener);
    }
}
