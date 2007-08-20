<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:param name="module"/>
    <xsl:param name="basedir"/>
    <xsl:param name="module_srcdir"/>
	<xsl:template match="/">
<document>
  <properties>
    <author email="qalab@objectlab.co.uk">Benoit Xhenseval</author>
    <title>QALab Movers Report</title>
  </properties>
  <!-- Optional HEAD element, which is copied as is into the XHTML <head> element -->
  <head>
	<META http-equiv="Content-Type" content="text/html; charset=US-ASCII"/>
	<title>QALab Movers and Shakers</title>
  </head>
  <body>
      <section name="QALab Movers and Shakers">
      <p>There is also an RSS Feed for <a href="qalab-movers-report.rss">this report <img src="images/rss.png"/></a>.</p>
	      <table>
		<thead>
		<tr>
		  <th>Date Run</th>
		  <th>Start Time Window</th>
		  <th>End Time Window</th>
		  <th>Types</th>
		</tr>
		</thead>
		<tbody>
		<tr>
			<td>
				<xsl:value-of select="moversreport/daterun"/>
			</td>
			<td>
				<xsl:value-of select="moversreport/datethreshold"/>
			</td>
			<td>
				<xsl:value-of select="moversreport/enddatethreshold"/>
			</td>
			<td>
				<xsl:value-of select="moversreport/types"/>
			</td>
		</tr>
		</tbody>
		</table>
		<p>The report is divided in 2 sections, the "Up" represents a list of increased violations in the
		selected statistics.  They should be fixed as soon a possible!  The "Down" section shows the violations
		that have been fixed recently.</p>
		<xsl:call-template name="up"/>
		<xsl:call-template name="down"/>
	</section>
   </body>
</document>
	</xsl:template>
	<xsl:template name="up">
		<xsl:variable name="upCount" select="count(moversreport/up/file)"/>
		<xsl:variable name="upTotal" select="sum(moversreport/up/*/diff/@diff)"/>
		<xsl:variable name="_title" select="concat('Up by ', $upCount, ' file(s), Up by ')"/>
		<xsl:variable name="title" select="concat($_title, $upTotal, ' Violations')"/>
		<subsection>
			<xsl:attribute name="name"><xsl:value-of select="$title"/></xsl:attribute>
		<table>
			<xsl:call-template name="headers"/>
			<tbody>
			<xsl:for-each select="moversreport/up/file">
				<xsl:call-template name="resultrow"/>
			</xsl:for-each>
			</tbody>
		</table>
		</subsection>
	</xsl:template>
	<xsl:template name="down">
		<xsl:variable name="downCount" select="count(moversreport/down/file)"/>
		<xsl:variable name="downTotal" select="sum(moversreport/down/*/diff/@diff)"/>
		<xsl:variable name="_title" select="concat('Down by ', $downCount, ' file(s), Down by ')"/>
		<xsl:variable name="title" select="concat($_title, $downTotal, ' Violations')"/>
		<subsection><xsl:attribute name="name"><xsl:value-of select="$title"/></xsl:attribute>
		<table>
			<xsl:call-template name="headers"/>
			<tbody>
			<xsl:for-each select="moversreport/down/file">
				<xsl:call-template name="resultrow"/>
			</xsl:for-each>
			</tbody>
		</table>
		</subsection>
	</xsl:template>
	<xsl:template name="headers">
	<thead>
		<tr>
			<th>File Name</th>
			<th>Type</th>
			<th>Previous Run</th>
			<th>Previous Value</th>
			<th>Current Run</th>
			<th>Current Value</th>
			<th>Diff</th>
		</tr>
	</thead>
	</xsl:template>
	<xsl:template name="resultrow">
<!--		<xsl:variable name="cvslink">
			<xsl:apply-templates mode="cvsLink" select="@name"/>
		</xsl:variable>
		-->
		<xsl:variable name="pathurl" select="translate(@path, '\', '/')"/>
		<xsl:variable name="srcfile" select="substring-before($pathurl,'.java')"/>
		<xsl:variable name="_filedotlink" select="translate($pathurl, '/', '.')"/>
		<xsl:variable name="filedotlink" select="substring-before($_filedotlink,'.java')"/>
		<xsl:variable name="fileurl" select="translate(@name, '\', '/')"/>
		<xsl:variable name="_filelink" select="translate($fileurl, '/', '_')"/>
		<xsl:variable name="filelink" select="translate($_filelink, '.', '_')"/>
<!--		<xsl:variable name="alternateColor">
			<xsl:if test="position() mod 2 = 1">#FFFFFF</xsl:if>
			<xsl:if test="position() mod 2 = 0">#E4CDFC</xsl:if>
		</xsl:variable>-->
		<xsl:for-each select="diff">
			<tr>
				<td>
					<xsl:if test="position() = 1">
					<xsl:choose>
						<xsl:when test="$srcfile != ''">
							<a><xsl:attribute name="href"><xsl:value-of select="concat('xref/' , $srcfile, '.html')"/></xsl:attribute><xsl:value-of select="$fileurl"/></a>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$fileurl"/>
						</xsl:otherwise>
					</xsl:choose>
					</xsl:if>
					<xsl:if test="position() != 1">
						<xsl:text> </xsl:text>
					</xsl:if>
				</td>
				<td>
					<xsl:if test="@type='checkstyle'">
						<a><xsl:variable name="_temp0" select="concat('',$module)"/><xsl:variable name="_temp1" select="concat($_temp0,'checkstyle-report.html#')"/><xsl:attribute name="href"><xsl:value-of select="concat($_temp1 , $filelink)"/></xsl:attribute>checkstyle</a>
					</xsl:if>
					<xsl:if test="@type='pmd'">
						<a><xsl:variable name="_temp0" select="concat('',$module)"/><xsl:variable name="_temp1" select="concat($_temp0,'pmd-report.html#')"/><xsl:attribute name="href"><xsl:value-of select="concat($_temp1 , $filelink)"/></xsl:attribute>pmd</a>
					</xsl:if>
					<xsl:if test="@type='findbugs'">
						<a><xsl:variable name="_temp0" select="concat('',$module)"/><xsl:variable name="_temp1" select="concat($_temp0,'findbugs-report.html#')"/><xsl:attribute name="href"><xsl:value-of select="concat($_temp1 , $filelink)"/></xsl:attribute>findbugs</a>
			    	</xsl:if>
					<xsl:if test="@type='cobertura-line'">
						<a><xsl:attribute name="href"><xsl:value-of select="concat('cobertura/' , $filedotlink, '.html')"/></xsl:attribute>cobertura-line</a>
			    	</xsl:if>
					<xsl:if test="@type='cobertura-branch'">
						<a><xsl:attribute name="href"><xsl:value-of select="concat('cobertura/' , $filedotlink, '.html')"/></xsl:attribute>cobertura-branch</a>
			    	</xsl:if>
				</td>
				<td align="center">
					<xsl:value-of select="@previousrun"/>
				</td>
				<td align="center">
					<xsl:value-of select="@previouserrors"/>
				</td>
				<td align="center">
					<xsl:value-of select="@currentrun"/>
				</td>
				<td align="center">
					<xsl:value-of select="@currenterrors"/>
				</td>
				<td align="center">
					<xsl:value-of select="@diff"/>
				</td>
			</tr>
		</xsl:for-each>
	</xsl:template>
<!--    <xsl:template mode="cvsLink" match="@*|node()">
        <xsl:variable name="fileurl" select="translate(., '\', '/')"/>
        <xsl:value-of select="concat($viewCvsPrefix, $module_srcdir, '/' , $fileurl)"/>
    </xsl:template>-->
</xsl:stylesheet>
