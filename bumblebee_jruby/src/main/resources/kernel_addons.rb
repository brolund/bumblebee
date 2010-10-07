module Kernel
    def method_missing(sym, *args)
        mess = "Missing Ruby method: " + sym.to_s + " on " + args.to_s
        raise mess
    end
end

class String 
    def lines from, length
        split("\n").slice(from-1,length).join("\n")
    end
    def escape_html
        string = to_s
        str = string ? string.dup : ""
        str.gsub!(/&/n, '&amp;')
        str.gsub!(/\"/n, '&quot;')
        str.gsub!(/>/n, '&gt;')
        str.gsub!(/</n, '&lt;')
        str
    end    
end


