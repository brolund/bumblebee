require 'java'
require 'bumblebee/source'
require 'bumblebee/from'

module Bumblebee
    class MethodObject
        def initialize(clazz, method_name, parameter_types)
            @clazz = clazz
            @method_name = method_name
            @parameters = parameter_types
        end
        def to_s
            arr = java.lang.String[@parameters.length].new
            @parameters.each_index{|index| arr[index] = @parameters[index]}
            Source.extractor.getCodeFromMethod(@clazz, @method_name, arr) 
        end
        def from
            From.new self
        end
    end
end
