require 'bumblebee/property_hash'

module Bumblebee
    module Traversal
        class Traversers
            include PropertyHashMixin, Enumerable
            def initialize
                @traversers=Array.new
            end
            def add(traverser)
                @traversers.push(traverser)
                self.send("#{traverser.phase}=".to_sym, traverser)
            end
            def replace(traverser, new_traverser)
                index = @traversers.index(traverser)
                @traversers[index]=new_traverser
                self.send("#{traverser.phase}=".to_sym, new_traverser)
                self.send("#{new_traverser.phase}=".to_sym, new_traverser)
            end
            def each &block
                @traversers.each &block
            end
            def run node
                @traversers.each {|traverser| node.traverse traverser}
            end
        end
    end
end
    