require 'bumblebee/method_object'
require 'bumblebee/variable_object'
require 'bumblebee/source'

module Bumblebee
    class Clazz 
        def initialize clazz, configuration
            @clazz = clazz
            @configuration = configuration
        end
        def method_missing(sym, *args)
            MethodObject.new @clazz, sym.to_s, args
        end
        def meth(name, *parameter_types)
            MethodObject.new @clazz, name, parameter_types
        end
        def variable(name)
            VariableObject.new @clazz, name
        end
        def to_s
            Source.extractor.getClassSource(@clazz) 
        end
        def source_link
            dir = "target/site"
            file_name = "#{@clazz}.html"
            File.open("#{dir}/#{file_name}", "w+") { |out|
                out.write '<?xml version="1.0" encoding="UTF-8"?>'
                out.write "\n<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>\n"
                out.write "<html><head>"
                out.write "<title>#{@clazz}.java</title>"
                #out.write "<link rel=\"stylesheet\" type=\"text/css\" href=\"#{File.basename(@configuration.stylesheet)}\"/>"
                out.write "</head><body><code><pre>"
                out.write( Source.extractor.getClassSource(@clazz) )
                out.write "</pre></code></body></html>"
            }
            file_name
    end
    end
end
