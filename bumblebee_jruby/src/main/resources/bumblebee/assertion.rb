require 'java'
include_class 'com.agical.bumblebee.ruby.invocation.ExceptionalInvocationResult'
module Bumblebee
    class Assertion
        def initialize assertions, node
            @assertions = assertions
            @node = node
        end
        def that(&assertion)
            @assertions.push(assertion)
        end 
        def equals (expected, actual, description ="Should be equal") 
            that do |node|
                clazz = node.current_class
                method = node.current_method
                if actual!=expected
                    desc = "\nActual  : #{actual}\nExpected: #{expected}\n"
                    desc += "\nDescription of assertion: " + description
                    add_assertion_failure node, description, clazz, desc, method
                else
                    add_assertion_success node, description, clazz, description, method
                end
            end
            ""
        end
        def is_true(value, description ="Should be true") 
            that do |node|
                clazz = node.current_class
                method = node.current_method
                if !value
                    desc = "\nExpected true"
                    desc += "\nDescription of assertion: " + description
                    add_assertion_failure node, description, clazz, desc, method
                else
                    add_assertion_success node, description, clazz, description, method
                end
            end
            ""
        end
        def not_contains(string_to_contain, description ="Node should not contain") 
            that do |node|
                clazz = node.current_class
                method = node.current_method
                if node.content.match(string_to_contain)||node.header.match(string_to_contain)
                    desc = "\n======================"
                    desc += "\nPresent in document: " + string_to_contain + "\nIn class: " + clazz
                    desc += "\nIn method " + method.to_s  if !method.nil?
                    desc += "\nDescription of assertion: " + description
                    desc += "\nText:\n" + node.header + "\n" + node.content
                    desc += "\n======================\n"
                   add_assertion_failure node, description + " - " + string_to_contain, clazz, desc, method
                else
                    add_assertion_success  node, description + " - " + string_to_contain , clazz, description, method
                end
            end
            ""
        end
        def contains(string_to_contain, description ="Node should contain")
            that do |node|
                clazz = node.current_class
                method = node.current_method
                matcher = Regexp.new(string_to_contain.gsub('[','\['), Regexp::MULTILINE)
                if !node.content.match(matcher)&&!node.header.match(matcher)
                    desc = "\n======================"
                    desc += "\nMissing in document: \n" + string_to_contain + "\nIn class: " + clazz
                    desc += "\nIn method: " + method.to_s  if !method.nil?
                    desc += "\nDescription of assertion: " + description
                    desc += "\nText:\n" + node.header + "\n" + node.content
                    desc += "\n======================\n"
                    add_assertion_failure node, description + " - " + string_to_contain, clazz, desc, method
                else
                    add_assertion_success  node, description + " - " + string_to_contain , clazz, description, method
                end
            end
            ""
        end
        def add_assertion_failure node, assertion, clazz, description = "<no description>", method = ""
            node.status_callback.verification_failed description, clazz, method, assertion
        end
        def add_assertion_success node, assertion, clazz, description = "<no description>", method = ""
            node.status_callback.verified description, clazz, method, assertion
        end
    end
end
