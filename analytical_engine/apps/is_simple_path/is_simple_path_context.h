/** Copyright 2020 Alibaba Group Holding Limited.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

#ifndef ANALYTICAL_ENGINE_APPS_IS_SIMPLE_PATH_IS_SIMPLE_PTAH_CONTEXT_H_
#define ANALYTICAL_ENGINE_APPS_IS_SIMPLE_PATH_IS_SIMPLE_PTAH_CONTEXT_H_

#include <limits>
#include <utility>
#include <vector>
#include <set>
#include "grape/grape.h"

#include "core/context/tensor_context.h"

namespace gs {

template <typename FRAG_T>
class IsSimplePathContext : public TensorContext<FRAG_T, typename FRAG_T::oid_t> {
 public:
  using oid_t = typename FRAG_T::oid_t;
  using vid_t = typename FRAG_T::vid_t;
  using vertex_t = typename FRAG_T::vertex_t;

  explicit IsSimplePathContext(const FRAG_T& fragment)
      : TensorContext<FRAG_T, typename FRAG_T::oid_t>(fragment) {}

  void Init(grape::DefaultMessageManager& messages, const std::vector<oid_t>& pathlist) {
    auto& frag = this->fragment();
    auto vertices = frag.Vertices();
    auto inner_vertices = frag.InnerVertices();

    is_simple_path = true;
    for (std::vector<vid_t>::iterator itr = pathlist.begin(); itr != (pathlist.end()-1); itr++)
      pair_list.push_back(make_pair(*itr,*(itr+1));

  }

  void Output(std::ostream& os) override {
    auto& frag = this->fragment();
    os<<is_simple_path<<std::endl;
  }

  std::set<oid_t> visit;
  std::vector<std::pair<oid_t,oid_t>> pair_list;
  int true_counter = 0;
  bool is_simple_path = true;
};
}  // namespace gs

#endif  // ANALYTICAL_ENGINE_APPS_IS_SIMPLE_PATH_IS_SIMPLE_PTAH_CONTEXT_H_