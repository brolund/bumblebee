require 'bumblebee/clazz'
require 'bumblebee/method_object'
require 'bumblebee/assertion'
require 'bumblebee/property_hash'

module Bumblebee
    module Model
        module Node
            include Bumblebee::PropertyHashMixin
            attr_accessor :object, :parent, :nodes, :header,:status_callback
            def parser= arg
                @parser = arg
            end
            def parser
                @parser
            end
            
            def initialize obj, status_callback
                @nodes=Array.new
                self.nodes = @nodes
                @assertions = Array.new
                @store = {}
                self.object=obj
                self.status_callback=status_callback
            end
            def add_node node
                @nodes.push node
                node.parent=self
                node
            end
            def store key, value
                @store[key]=value
            end
            def set_header(header_name)
                self.header=header_name
                ""
            end
            def assertions
                @assertions
            end
            def snip object
                ">>>>\n#{object.to_s}\n<<<<\n"
            end
            def get_value(key)
                @store[key]
            end
            def assert
                Bumblebee::Assertion.new @assertions, self
            end
            def configuration
                self.parent.configuration
            end
            def to_s
                "Node: #{self.object}"
            end
            def exec *args
                begin
                    Kernel.exec args
                rescue Exception => e
                    puts e.class
                end
            end
        end
    end
end