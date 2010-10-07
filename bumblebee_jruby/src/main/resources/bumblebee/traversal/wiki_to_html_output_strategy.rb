module Bumblebee
    module Traversal
        class WikiToHtmlOutputStrategy
            def initialize wiki_parser=WikiParser.new
                @wiki_syntax = WikiSyntax.new
            end
            def start_document configuration_node
                java.io.File.new(configuration_node.target_file).getParentFile().mkdirs
                stylesheet_target_dir = File.dirname(configuration_node.target_file)
                stylesheet_file = "bumblebee-default-stylesheet.css"
                File.open(stylesheet_target_dir +  "/" + stylesheet_file, "w") {|out|
                    out.write get_stylesheet 
                }
                if(configuration_node.stylesheet?&&File.exists?(configuration_node.stylesheet.to_s))
                    stylesheet_file = File.basename(configuration_node.stylesheet)
                    configuration_node.copy(configuration_node.stylesheet, stylesheet_target_dir + "/" + stylesheet_file)
                end                
                @out = File.open(configuration_node.target_file, "w")
                @out.write '<?xml version="1.0" encoding="UTF-8"?>'
                @out.write "\n<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>\n"
                @out.write "<html><head>"
                @out.write "<title>#{configuration_node.document_title}</title>"
                @out.write "<link rel=\"stylesheet\" type=\"text/css\" href=\"#{stylesheet_file}\"/>"
                @out.write "</head><body>"
                @out.write "<h1>#{configuration_node.document_title}</h1>"
                @out.write configuration_node.configuration.html_index
            end
            def write_header root_node, reference, level
                @out.write "<a name=\"#{reference}\"/>"
                @out.write '<h1>'
                @out.write @wiki_syntax.convertToInlineHtml(root_node.header)
                @out.write '</h1>' + "\n"
            end 
            def write_sub_header root_node, reference, level
                @out.write "<a name=\"#{reference}\"/>"
                @out.write '<h2>'
                @out.write @wiki_syntax.convertToInlineHtml(root_node.header)
                @out.write '</h2>' + "\n"
            end 
            def start_block node, level, type
                @out.write "<div class=\"#{type}\">\n"
            end             
            def end_block node, level, type
                @out.write "</div>\n"
            end             
            def write_wiki node
                @out.write @wiki_syntax.convertToHtml(node.content)
            end
            def end_document configuration_node
                incep = configuration_node.inception_year.to_s + "-" + Time.new.year.to_s
                if(configuration_node.inception_year-Time.new.year==0) 
                    incep = Time.new.year.to_s
                end
                @out.write "&#169; #{incep} #{configuration_node.copyright}</body>\n"
                @out.write "</html>\n"
                @out.close
            end
        end
    end
end

