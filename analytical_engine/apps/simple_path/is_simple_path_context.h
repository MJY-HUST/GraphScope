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

#ifndef ANALYTICAL_ENGINE_APPS_SIMPLE_PATH_IS_SIMPLE_PATH_CONTEXT_H_
#define ANALYTICAL_ENGINE_APPS_SIMPLE_PATH_IS_SIMPLE_PATH_CONTEXT_H_

#include <limits>
#include <set>
#include <utility>
#include <vector>

#include "folly/dynamic.h"
#include "folly/json.h"
#include "grape/grape.h"

#include "core/context/tensor_context.h"

namespace gs {

template <typename FRAG_T>
class IsSimplePathContext : public TensorContext<FRAG_T, bool> {
 public:
  using oid_t = typename FRAG_T::oid_t;
  using vid_t = typename FRAG_T::vid_t;
  using vertex_t = typename FRAG_T::vertex_t;

  explicit IsSimplePathContext(const FRAG_T& fragment)
      : TensorContext<FRAG_T, typename FRAG_T::oid_t>(fragment) {}

  /**
   * @brief json formate
   *  josn = {"nodes" : [(oid_t)node1,(oid_t)node2,....]}
   *
   * @param messages
   * @param nodes_json
   */

  void Init(grape::DefaultMessageManager& messages,
            const std::string& nodes_json) {
    std::set<oid_t> visit;
    is_simple_path = true;
    int counter = 0;
    oid_t pair_1;

    folly::dynamic::array nodes_array = folly::json(nodes_json);
    for (auto val : nodes_array) {
      oid_t key = (oid_t) val;
      if (!visit.count(key)) {
        visit.insert(key);
      } else {
        is_simple_path = false;
        break;
      }
      counter++;
      if (counter == 1) {
      } else {
        pair_list.push_back(make_pair(pair_1, key));
      }
      pair_1 = key;
    }
  }

  void Output(std::ostream& os) override {
    auto& frag = this->fragment();
    os << is_simple_path << std::endl;
  }

  std::vector<std::pair<oid_t, oid_t>> pair_list;
  int true_counter = 0;
  bool is_simple_path;
};
}  // namespace gs

#endif  // ANALYTICAL_ENGINE_APPS_SIMPLE_PATH_IS_SIMPLE_PATH_CONTEXT_H_
