require 'java'
require 'bumblebee/traversal/abstract_traverser'
require 'bumblebee/source'
require 'bumblebee/model/node'


module Bumblebee
    module Model
        module Node
            attr_accessor :content
        end
    end
end

module Bumblebee
    module Traversal
        class CommentExecuterTraverser < AbstractTraverser
            def initialize
                super
                self.phase = :commentexecuter
                self.description = "Executes the comments as Ruby string templates"               
                on_enter_class {|node| do_for_class node}
                on_enter_method {|node| do_for_method node}
                on_enter_thrower {|node| do_for_thrower node}
            end
            def do_for_class node
                comment = Bumblebee::Source.extractor.getClassComment(node.object.getName())
                execute_comment(node, comment)
            end
            def do_for_method node
                comment = Bumblebee::Source.extractor.getAutoSnippetingComment(node.parent.object.getName(), node.object.getName())
                execute_comment(node, comment)
            end
            def do_for_thrower node
                comment = Bumblebee::Source.extractor.getAutoSnippetingComment(node.parent.object.getName(), node.object.getName())
                node.content = comment
            end
            def execute_comment(node, comment) 
                begin
                    node.content=node.instance_eval('"' + comment + '"')
                rescue Exception => e
                    puts e.class
                    puts "========= Bad comment =========\n" + comment.to_s + "========= End Bad comment =========\n" 
                    raise e
                end
            end
        end
    end
end

