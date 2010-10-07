module Bumblebee
    module Traversal
        class AbstractTraverser
            attr_accessor :phase, :description
            def self.node_types *syms
                @@all_types ||= Array.new
                syms.each do |sym|
                    @@all_types.push(sym)
                end
            end
            def init_types
                @types = Hash.new
                @excluded_nodes = Array.new
                @@all_types.each do |sym|
                    hash = Hash.new 
                    hash[:enter]=Array.new
                    hash[:exit]=Array.new
                    @types[sym.to_sym]=hash
                end
                @types
            end
            def exclude node
                @excluded_nodes.push node
            end            
            node_types :root, :class, :method, :thrower
             
 
            def method_missing sym, *args, &block
                begin
                    @types ||= init_types
                     if(sym.to_s.index('on').eql?(0))
                         on,preposition,type = sym.to_s.split('_')
                        @types[type.to_sym][preposition.to_sym].push(block)
                     else
                        if(!@excluded_nodes.include?(args[0]))
	                        preposition,type = sym.to_s.split('_')
	                        @types[type.to_sym][preposition.to_sym].each {|closure| closure.call(args[0])}
                        end
                    end
                rescue Exception => e
                    node = args.nil? ? "<no node information>":args[0] 
                    message = "Got an exception:\n#{e}\nIn: #{node}\nIn block:\nStacktrace:" + e.backtrace.join("\n") + "\n#{block}"
                    puts message
                    if(node) 
                        node.status_callback.verification_failed message, node.current_class.to_s, node.current_method.to_s, "Unexpected exception"
                    end
                end    
            end     
        end
    end
end
