package com.agical.bumblebee.parser;

import org.junit.Before;
import org.junit.Ignore;

public class SomeClassWithTabs {
	final public static String C1 = "C1", C2 = "C2";
	final public static String[] ALL = { C1, C2 };

	/*!!
	Class comment
	
		indented part
		
	*/
	
	public void mended() {
		/*!
		A comment
		*/
		System.out.println("Bug fixed?");
	}
	public void emptyMethod() {}

	
	public void method() {
		String s = "s";
	}
	
	/**
	 * Some JavaDoc
	 * With several lines
	 * @param p
	 * @param i
	 * @return
	 * @throws IllegalArgumentException
	 */
	public String trickyMethod(String p, int i) throws IllegalArgumentException {
		i++;
		return p;
	}
		
	public String trickyMethod(String p, Long lng) {
		lng++;
		return p;
	}

	@Before
	@Ignore
	public void multiLineAnnotations() {
	}
	
	public String nonOverloadedMethodWithArguments(String s, Long lng) {
		return s + lng;
	}
	
	public void methodWithSingleComment() {
	/*!marker
	doc
	*/
	}
	
	public void methodWithIndentedComment() {
	/*!marker
	doc
		single indent
			double indent
			
	back to unindented after empty line
	*/
	}
	
	public void methodWithTwoComments() {
		/*!marker1
		doc1
		*/
		String codeInBetween = "\n";
		{
			boolean blocked = false;
		}
		/*!marker2
		doc2
		*/
		Integer moreCode = 666;
	}
	public void methodWithEmptyMarkers() {
		/*!marker1*/
		String codeInBetweenEmptyMarkers = "";
		{
			boolean blocked = false;
		}
		/*!marker2
		More text
		*/
		String moreCode = "here";
		/*!marker3*/
	}
	public void methodForAutoSnippeting() {
		String notInOutput = "Because it is before the first comment";
		/*!
		First comment
		*/
		String snippetedCode = "Because it is between comments";
		String htmlEscaped = "< > &\n";
		String rubyStringEscaped = "#{";
		{
			boolean blocked = false;
		}
		/*!*/
		Integer notInOutputEitherSinceItIsAfterTheLastBumblebeeComment = 666;
	}
	
	public void usingSmallerCodeSnippets() throws Exception {
		/*!m1
		Consider the following code:
		>>>>
		#{meth}
		<<<<
		=meth.from.m1.to.m2= retrives the code between the comments with the 
		markers (=m1= and =m2= in this case) and it looks like this:
		>>>>
		#{meth.from.m1.to.m2}
		<<<<
		Note that the last variable is not in the snippet.
		*/
		String exampleCode = "Hello, I'm a code snippet!";
		/*!m2
		Note also that there can be several comments in one file, but the comments can be 
		empty (contain only the marker) to serve as markers
		#{assert.contains 'String'+' exampleCode', 'Can include smaller code snippets'}
		*/
		boolean thisVariableWillNotBeInTheSnippet = true;
		/*!m3*/
	}

	/**
	 * JavaDoc for runnable
	 */
	public static final Runnable runnable = new Runnable() {
		public void run() {
			// empty
		}
	};
}