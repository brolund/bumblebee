require 'bumblebee/clazz'
require 'bumblebee/method_object'
require 'bumblebee/assertion'

module Bumblebee
    module Model
        class MethodNode
            include Bumblebee::Model::Node
            def traverse(visitor)
                @traversing.call(self,visitor,@nodes)
            end
            def finished_normally(invocation)
                self.execution=invocation
                @traversing = Proc.new do |node,visitor,child_nodes|
                    visitor.enter_method node
                    child_nodes.each do |child| 
                        child.traverse visitor
                    end
                    visitor.exit_method node
                end
            end
            def threw_exception(invocation)
                self.execution=invocation
                @traversing = Proc.new do |node,visitor,child_nodes|
                    visitor.enter_thrower node
                    child_nodes.each do |child| 
                        child.traverse visitor
                    end
                    visitor.exit_thrower node
                end
            end
            def meth *parameter_types
                Bumblebee::MethodObject.new parent.object.getName(), self.object.getName(), parameter_types
            end
            def current_class
                self.parent.object.getName()
            end
            def current_method
                self.object.getName()
            end
            def clazz cl=current_class
                Bumblebee::Clazz.new(cl, configuration)
            end
        end
    end
end