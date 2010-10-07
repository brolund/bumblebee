include_class 'com.agical.bumblebee.parser.WikiSyntax'

module Bumblebee
    module Traversal
        class WikiParser
            def initialize 
                @wiki_syntax = WikiSyntax.new
            end
            def parse text, html_writer
                html_writer.write_plain_text @wiki_syntax.convertToHtml(text) 
            end
            def parse_for_inline text, html_writer
                html_writer.write_plain_text @wiki_syntax.convertToHtml(text).match('<p>(.*)</p>')[1]
            end
        end
    end
end