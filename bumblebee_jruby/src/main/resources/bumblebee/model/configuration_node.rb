require 'bumblebee/clazz'
require 'bumblebee/method_object'
require 'bumblebee/assertion'


module Bumblebee
    module Model
        class ConfigurationNode
            include Bumblebee::Model::Node
            def traverse visitor
                visitor.enter_root self
                @nodes.each {|node| node.traverse visitor}
                visitor.exit_root self
            end
            def configuration
                self
            end
            def current_class
                @nodes[0].object.getName()
            end
            def current_method
                @nodes[0].object.getName()
            end            
        end    
    end
end