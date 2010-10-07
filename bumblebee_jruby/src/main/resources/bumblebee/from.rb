require 'bumblebee/source'

module Bumblebee
    class From
        def initialize(object)
            @object = object
            @m = []
            @is_limited = false
        end 
        def method_missing sym, *args
            @m.push sym.to_s
            self
        end
        def to_s
            if(@is_limited) 
                Source.extractor.getCodeBetweenCommentMarkers(@m[0], @m[1], @object.to_s)
            else
                Source.extractor.getCodeAfterCommentMarker(@m[0], @object.to_s)
            end
        end
        def to
            @is_limited = true
            self
        end
    end
end
