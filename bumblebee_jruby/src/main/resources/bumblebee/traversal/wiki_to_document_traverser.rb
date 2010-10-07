require 'java'
require 'bumblebee/traversal/abstract_traverser'
require 'bumblebee/traversal/html_index_traverser'
require 'bumblebee/traversal/stylesheet'
require 'bumblebee/traversal/wiki_parser'
require 'bumblebee/traversal/wiki_to_html_output_strategy'
require 'bumblebee/source'

module Bumblebee
    module Traversal
        class WikiToDocumentTraverser < AbstractTraverser
            attr_accessor :output_strategy
            def initialize output_strategy=WikiToHtmlOutputStrategy.new
                self.phase = :wiki2doc
                self.description = "Converts the result of the executer from wiki to html"               
                self.output_strategy = output_strategy
                @level = 0
                on_enter_root {|node|
                    output_strategy.start_document node
                    @level += 1
                }
                on_exit_root {|node| 
                    output_strategy.end_document node
                    @level -= 1
                    execute_assertions(node)
                }
                on_enter_class {|node|
                    output_strategy.write_header node, "#{node.object.getName()}", @level
                    output_strategy.write_wiki node
                    @level += 1
                }
                on_exit_class {|node|
                    @level -= 1
                }
                on_enter_method {|node|
                    output_strategy.write_sub_header node, "#{node.parent.object.getName()}.#{node.object.getName()}", @level
                    output_strategy.write_wiki node
                    @level += 1
                }
                on_exit_method {|node|
                    @level -= 1
                }
                on_enter_thrower {|node|
                    output_strategy.start_block(node, @level, :thrower)
                    output_strategy.write_sub_header node, "#{node.parent.object.getName()}.#{node.object.getName()}", @level
                    output_strategy.write_wiki node 
                    @level += 1
                }
                on_exit_thrower {|node|
                    output_strategy.end_block(node, @level, :thrower)
                    @level -= 1
                }
            end

            def assert(node, description, &assertion)
                @assertions ||= Array.new
                @assertions.push [description, assertion, node]
            end
            def execute_assertions root_node
                File.open(root_node.configuration.target_file) {|io|
                    content = io.read
                    @assertions ||= Array.new
                    @assertions.each{|assertion| 
                        if(!assertion[1].call(content))
                            throw Exception.new("Document assert: #{assertion[0]}\nOn node:\n#{assertion[2].object.to_s}")
                        end
                    }
                }
            end
        end
    end
end
