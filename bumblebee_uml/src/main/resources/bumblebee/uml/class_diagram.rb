require 'bumblebee/method_object'
require 'bumblebee/variable_object'
require 'bumblebee/source'
require 'java'

module Bumblebee
    module Model
        module Node
            def uml
                Bumblebee::Uml::Uml.new(self)                
            end
        end        
    end
end

module Bumblebee
    module Uml
        class Uml
            def initialize node
                @node = node
            end           
            def classdiagram
                Bumblebee::Uml::ClassDiagram.new(@node)
            end
        end
        
        class ClassDiagram
            def initialize node
                @node = node
                @clazz = node.current_class
                @focus_clazz = node.current_class
                @uml = com.agical.bumblebee.uml.UmlspeedBuilder.new(Source.parser)
            end
            def focused_on focus_clazz
                 @focus_clazz = focus_clazz
                 self
            end
            def for_all classes
                @classes = classes      
                self         
            end
            def to_s
                if @classes!=nil
                    to_s_method
                else
                    to_s_classes  
                end
            end
            def to_s_classes
                begin
                    basename = @clazz.to_s+"."+@node.current_method.to_s
                    @uml.classdiagramForMethod("target/site/images/", basename, @clazz, @node.current_method, @focus_class)
                    return '[[images/' + basename + '.png][]]'
                rescue Exception => e
                    puts e
                end
            end
            def to_s_method
                begin
                    basename = @clazz.to_s+"."+@node.current_method.to_s + ".classes"
                    @uml.classdiagramForClasses("target/site/images/", basename, @classes)
                    return '[[images/' + basename + '.png][]]'
                rescue Exception => e
                    puts e
                end
            end
        end
    end
end
