require 'bumblebee/model/configuration_node'
require 'bumblebee/model/class_node'
require 'bumblebee/model/method_node'

module Bumblebee
    class BumblebeeStructure 
        attr_accessor :status_callback
        def start_script
            @node = Bumblebee::Model::ConfigurationNode.new 'Bumblebee',status_callback
            Bootstrapper.new.configure_defaults @node
        end
        def begin_structure(java_class)
            @node = @node.add_node(Bumblebee::Model::ClassNode.new(java_class,status_callback))
        end
        def begin_method(invocation)
            @node = @node.add_node(Bumblebee::Model::MethodNode.new(invocation.getMethod(),status_callback))
        end
        def store_data(key, data)
            @node.method_missing((key+"=").to_sym, data)
            @node.store key, data
        end
        def end_method(invocation)
            @node.finished_normally(invocation)
            @node = @node.parent
        end
        def end_method_with_exception(invocation)
            @node.threw_exception(invocation)
            @node = @node.parent
        end
        def end_structure(execution)
            @node.execution=execution
            @node = @node.parent
        end
        def execute_structure execution_time
            @node.traversers.run @node
        end
    end
end
