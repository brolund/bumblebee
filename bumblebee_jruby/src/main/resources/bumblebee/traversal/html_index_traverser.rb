require 'bumblebee/traversal/abstract_traverser'

module Bumblebee
    module Traversal
        class HtmlIndexTraverser < AbstractTraverser
            def initialize
                super
                self.phase = :htmlindex
                self.description = "Creates an html index"
                on_enter_root {|node| 
                    node.configuration.html_index =""
                    node.configuration.html_index += '<h3>Index</h3><ul>'
                }
                on_enter_class {|node| node.configuration.html_index += "<li><a href=\"##{node.object.getName()}\">#{node.header}</a></li><ul>"}
                on_exit_class {|node| node.configuration.html_index += "</ul>"}
                on_enter_method {|node| node.configuration.html_index += "<li><a href=\"##{node.parent.object.getName()}.#{node.object.getName()}\">#{node.header}</a></li><ul>"}
                on_exit_method {|node| node.configuration.html_index += "</ul>"}
                on_enter_thrower {|node| node.configuration.html_index += "<li><div class=\"thrower\"><a href=\"##{node.parent.object.getName()}.#{node.object.getName()}\">#{node.header}</a></div></li><ul>"}
                on_exit_thrower {|node| node.configuration.html_index += "</ul>"}
                on_exit_root {|node| node.configuration.html_index += "</ul>"}
            end
        end
    end
end
