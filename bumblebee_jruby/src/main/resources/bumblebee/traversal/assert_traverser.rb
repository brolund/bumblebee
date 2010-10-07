require 'bumblebee/traversal/abstract_traverser'

module Bumblebee
    module Traversal
        class AssertTraverser < AbstractTraverser
            def assert_node node
                node.assertions.each do |assertion| 
                    assertion.call(node)
                end 
            end
            
            def initialize
                super
                self.phase = :assert
                self.description = "Executes all asserts"                
                on_enter_class {|node| assert_node node}
                on_enter_method {|node| assert_node node}
                on_enter_thrower {|node| assert_node node}
            end

        end
    end
end
