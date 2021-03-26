#!/usr/bin/env python3
# -*- coding: utf-8 -*-
#
# Copyright 2020 Alibaba Group Holding Limited. All Rights Reserved.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#


from graphscope.framework.app import AppAssets
from graphscope.framework.app import not_compatible_for

__all__ = ["triangles"]


@not_compatible_for("arrow_property", "dynamic_property")
def triangles(graph):
    """Evaluate triangle counting of the graph G.

    Args:
        graph (:class:`Graph`): A projected simple graph.

    Returns:
        :class:`VertexDataContext`: A context with each vertex assigned with the triangle counting result.

    Examples:

    .. code:: python

        import graphscope as gs
        sess = gs.session()
        g = sess.g()
        pg = g.project_to_simple(v_label='vlabel', e_label='elabel')
        r = gs.triangles(pg)
        s.close()

    """
    return AppAssets(algo="triangles")(graph)
