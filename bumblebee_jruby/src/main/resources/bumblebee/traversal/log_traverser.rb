require 'bumblebee/traversal/abstract_traverser'

module Bumblebee
    module Traversal
        class LogTraverser < AbstractTraverser
            def initialize
                self.phase = :log
                self.description = "Writes the nodes to stdout"               
                on_enter_root {|node| "Root: {"}
                on_exit_root {|node| log "}"}
                on_enter_class {|node| log node.object.getName() + "{"}
                on_exit_class {|node| log "}"}
                on_enter_method {|node| log node.object.getName() + "{"}
                on_exit_method {|node| log "}"}
                on_enter_thrower {|node| log "F:" +node.object.getName() + "{"}
                on_exit_thrower {|node| log "}"}
            end
            def log str
                puts str
            end
        end
    end
end
