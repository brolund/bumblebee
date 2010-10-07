require 'bumblebee/traversal/comment_executer_traverser'

module Bumblebee
    module Traversal
        class CommentExecuterTraverser < AbstractTraverser
            def do_for_method node
                comment = Bumblebee::Source.extractor.getCommentsFromMethod(node.parent.object.getName(), node.object.getName())
                node.content=node.instance_eval('"' + comment + '"')
            end
        end
    end
end