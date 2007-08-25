<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="html" 
     indent="yes"  />
<xsl:param name="indexpage" select="index.html"/>
<xsl:param name="project" select="files"/>

<xsl:template match="/qalab">
    <html>
    <head>
    <META HTTP-EQUIV="Content-Style-Type" CONTENT="text/css"/>
    <title>QALab Results from <xsl:value-of select="$project"/></title>
    <link rel="stylesheet" type="text/css" href="../reports.css"/>
    </head>
    <body>
    <h1>QALab Results from <xsl:value-of select="$project"/> source code</h1>
    <p align="right">Run with <a href="http://qalab.sourceforge.net">QALab</a> on <xsl:value-of select="@time"/></p>
    <hr size="2" />
    <table class="details" align="center">
        <tr>
        <th>Measure</th>
        <th>Trends</th>
        <th>Recent Changes</th>
        </tr>
        <xsl:apply-templates/>
    </table>
    </body>
    </html>
</xsl:template>

<xsl:template match="//tool">
    <tr>
    <td><xsl:value-of select="@name"/></td>
    <td><a><xsl:attribute name="href"><xsl:value-of select="@chart"/></xsl:attribute>Chart</a></td>
    <td><a><xsl:attribute name="href"><xsl:value-of select="@movers"/></xsl:attribute>Movers</a></td>
    </tr>
    <p/>
</xsl:template>

</xsl:stylesheet>
