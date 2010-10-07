require 'bumblebee/clazz'
require 'bumblebee/method_object'
require 'bumblebee/assertion'

module Bumblebee
    module Model
        class ClassNode
            include Bumblebee::Model::Node
            def traverse visitor
                visitor.enter_class self
                @nodes.each {|node| node.traverse visitor}
                visitor.exit_class self
            end
            def current_class
                self.object.getName()
            end
            def current_method
                nil
            end
            def clazz cl=current_class
                Bumblebee::Clazz.new(cl, configuration)
            end
        end
    end
end