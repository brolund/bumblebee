require 'java'

include_class 'com.agical.bumblebee.parser.PmdSourceExtractor'
include_class 'com.agical.bumblebee.parser.PmdParser'

module Bumblebee
    class Source
        roots = ""
        $:.each {|path| roots += path + ","}
        @@parser = PmdParser.new(roots)
        @@extractor = PmdSourceExtractor.new(@@parser)
        def Source.extractor
            @@extractor
        end
        def Source.parser
            @@parser
        end
    end
end
