<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="html" 
     indent="yes"  />
<xsl:param name="indexpage" select="index.html"/>

<xsl:template match="/project-report">
    <html>
    <head>
    <META HTTP-EQUIV="Content-Style-Type" CONTENT="text/css"/>
    <title>Feedback from <xsl:value-of select="@name"/></title>
    <link rel="stylesheet" type="text/css" href="reports.css"/>
    </head>
    <body>
    <h1>Feedback from <xsl:value-of select="@name"/> source code</h1>
    <p align="right">Run with <a href="http://jbrugge.com/glean">Glean</a> and <a href="http://ant.apache.org">Ant</a> on <xsl:value-of select="@time"/></p>
    <hr size="2" />
    <table class="details" align="center">
        <tr>
        <th>Report name</th>
        <th>Description</th>
        </tr>
        <xsl:apply-templates/>
    </table>
    </body>
    </html>
</xsl:template>

<xsl:template match="//report">
    <tr>
    <td><a><xsl:attribute name="href"><xsl:value-of select="@name"/>/<xsl:value-of select="@indexpage"/></xsl:attribute><xsl:value-of select="@name"/></a></td>
    <td><xsl:value-of select="@description"/></td>
    </tr>
    <p/>
</xsl:template>

</xsl:stylesheet>
