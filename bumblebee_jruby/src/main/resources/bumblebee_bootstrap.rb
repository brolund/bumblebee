require 'bumblebee/traversal/decamelcase_header_traverser'
require 'bumblebee/traversal/comment_executer_traverser'
require 'bumblebee/traversal/html_index_traverser'
require 'bumblebee/traversal/wiki_to_document_traverser'
require 'bumblebee/traversal/assert_traverser'
require 'bumblebee/traversal/log_traverser'
require 'bumblebee/traversal/traversers'
require 'bumblebee/bumblebee_structure'
require 'bumblebee/model/extension/node_manipulation'
require 'kernel_addons'
require 'ftools'
require 'java'

class Bootstrapper
    def configure_defaults(configuration)
        configure_document(configuration)
        configure_traversers(configuration)
    end
    
    def configure_document(configuration)
        configuration.document_title = 'Documentation'
        configuration.target_file = "target/site/documentation.html"
        configuration.inception_year = Time.new.year
        configuration.copyright = 'Copyright'
    end
    
    def configure_traversers(configuration) 
        decamelcaser = Bumblebee::Traversal::DeCamelCaseHeaderTraverser.new
        comment_executer = Bumblebee::Traversal::CommentExecuterTraverser.new 
        asserter = Bumblebee::Traversal::AssertTraverser.new
        wiki2doc = Bumblebee::Traversal::WikiToDocumentTraverser.new
        htmlindex = Bumblebee::Traversal::HtmlIndexTraverser.new
        
        configuration.traversers=Bumblebee::Traversal::Traversers.new
        configuration.traversers.add(decamelcaser) 
        configuration.traversers.add(comment_executer) 
        configuration.traversers.add(asserter)
        configuration.traversers.add(htmlindex)
        configuration.traversers.add(wiki2doc)
    #        configuration.traversers.add(Bumblebee::Traversal::LogTraverser.new, "log")
    end
end

IR=com.agical.bumblebee.ruby.invocation.InvocationResult
class IR
    def on_exception &closure
    end
    def on_completion &closure
        closure.call
    end
end
# Got a weird exception when spelling out the class name, hence the abreviation
EIR=com.agical.bumblebee.ruby.invocation.ExceptionalInvocationResult
class EIR
    def on_exception &closure
        closure.call
    end
    def on_completion &closure
    end
end        

def copy from, to
    File.makedirs(File.dirname(to))
    File.copy(from, to)
end
