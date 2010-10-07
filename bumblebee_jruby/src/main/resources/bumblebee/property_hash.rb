module Bumblebee
    module PropertyHashMixin
        def method_missing sym, *args
            @configurations||=Hash.new
            if(sym.to_s.match('^(.*)=$')!=nil)
                name = sym.to_s.match('^(.*)=$')[1]
                @configurations[name] = args[0]
                return ''
            elsif(sym.to_s.match('^(.*)\?$')!=nil)
                name = sym.to_s.match('^(.*)\?$')[1]
                return @configurations[name]!=nil
            elsif(@configurations[sym.to_s]!=nil) 
                return @configurations[sym.to_s]
            else
                str = "No configuration parameter by the name '#{sym}' is set.\nYou can set it implicitly by invoking #{sym}=your_value"
                raise Exception.new(str)
            end
        end      
        def add_(key, value)
            @configurations||=Hash.new
            @configurations[key]=value
        end           
    end
    class PropertyHash
        include PropertyHashMixin
    end
end
                    