def get_stylesheet
default_stylesheet=<<END_OF_STYLESHEET


/* Basic text styling */
body {
  font: 0.9em Verdana, Arial, Helvetica, sans-serif;
  color: #000000;
  width: 900px;
}

/* Turn off image borders */
img {
  border: none;
}
pre {
  background: #bbeebb; 
  color: black; 
  border:1px solid #000000; 
  padding:5px
}
/* set text sizes */
h1 {
    font: normal 2em Verdana, Helvetica, Arial, sans-serif;
    color: #708074;
    padding-bottom: 0em;
}
h2 {
    font: 1.5em Verdana, Helvetica, Arial, sans-serif;
    color: #708074;
    border-bottom: 1px dotted #708074;
    padding-bottom: 0em;
}

h3, h4, h5 {
    font: bold 1.1em Verdana, Helvetica, Arial, sans-serif;
    color: #708074;
    border-bottom: 1px dotted #708074;
    padding-bottom: 0em;
}

/* set link styles */
a:link, a:visited {
    color: #708074;
    text-decoration: underline;
}

table,td,tr{
    border: 1px solid #dddddd; 
}

div.thrower h2, li div.thrower{
    color: #990000;
    background: #ffbbbb; 
    font-weight: bold
}

END_OF_STYLESHEET
return default_stylesheet
end