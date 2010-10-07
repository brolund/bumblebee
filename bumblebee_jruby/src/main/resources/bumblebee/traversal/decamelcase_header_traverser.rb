require 'java'
require 'bumblebee/traversal/abstract_traverser'

include_class 'com.agical.bumblebee.agiledox.DeCamelCasingFormatter'

module Bumblebee
    module Traversal
        class DeCamelCaseHeaderTraverser < AbstractTraverser
            def initialize
                super
                self.phase = :decamelcase
                self.description = "De-camel cases the class names and method names for section headers"
                on_enter_class {|node| node.header=DeCamelCasingFormatter.new.deCamelCase(node.object.getSimpleName())}
                on_enter_method {|node| node.header=DeCamelCasingFormatter.new.deCamelCase(node.object.getName())}
                on_enter_thrower {|node| node.header=DeCamelCasingFormatter.new.deCamelCase(node.object.getName())}
            end
        end
    end
end

