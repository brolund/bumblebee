require 'java'
require 'bumblebee/source'
require 'bumblebee/from'

module Bumblebee
    class VariableObject
        def initialize(clazz, variable_name)
            @clazz = clazz
            @variable_name = variable_name
        end
        def to_s
            Source.extractor.getFieldDeclaration(@clazz, @variable_name) 
        end
        def from
            From.new self
        end
    end
end
