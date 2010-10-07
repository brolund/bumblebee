module Bumblebee
    module Model
        module Node
            def exclude
                configuration.traversers.wiki2doc.exclude(self)
                configuration.traversers.htmlindex.exclude(self)
            end
            def include_node_content &matcher
                this_node = self;
                configuration.traversers.wiki2doc.on_enter_class {|parent| 
                    parent.nodes.each {|node|
                        if(matcher.call(node))
                            this_node.content=this_node.content+node.content
                        end
                    }
                }
            end
        end
    end
end
    