<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:param name="module"/>
    <xsl:param name="basedir"/>
    <xsl:param name="module_srcdir"/>
	<xsl:template match="/">
		<html>
			<head>
				<META http-equiv="Content-Type" content="text/html; charset=US-ASCII"/>
				<title>QALab Movers and Shakers</title>
			</head>
			<body>
				<h1>Build Statistics Movers and Shakers</h1>
				<table cellspacing="0" cellpadding="5" border="0">
					<tr bgcolor="#8C66CC">
						<th>
							<font color="white">
								<b>Date Run</b>
							</font>
						</th>
						<th>
							<font color="white">
								<b>Start Cut Off Date</b>
							</font>
						</th>
						<th>
							<font color="white">
								<b>End Cut Off Date</b>
							</font>
						</th>
						<th>
							<font color="white">
								<b>Types</b>
							</font>
						</th>
					</tr>
					<tr valign="middle">
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
				</table>
		<p>The report is divided in 2 sections, the "Up" represents a list of increased violations in the
		selected statistics.  They should be fixed as soon a possible!  The "Down" section shows the violations
		that have been fixed recently.</p>
				<xsl:call-template name="up"/>
				<xsl:call-template name="down"/>
			</body>
		</html>
	</xsl:template>
	<xsl:template name="up">
		<xsl:variable name="upCount" select="count(moversreport/up/file)"/>
		<xsl:variable name="upTotal" select="sum(moversreport/up/*/diff/@diff)"/>
		<h3>Up by <xsl:value-of select="$upCount"/> file(s), Up by <xsl:value-of select="$upTotal"/> Violations...</h3>
		<table cellpadding="1" border="0" cellspacing="0">
			<xsl:call-template name="headers"/>
			<xsl:for-each select="moversreport/up/file">
				<xsl:call-template name="resultrow"/>
			</xsl:for-each>
		</table>
	</xsl:template>
	<xsl:template name="down">
		<xsl:variable name="downCount" select="count(moversreport/down/file)"/>
		<xsl:variable name="downTotal" select="sum(moversreport/down/*/diff/@diff)"/>
		<h3>Down by <xsl:value-of select="$downCount"/> file(s), Down by <xsl:value-of select="$downTotal"/> Violations...</h3>
		<table cellpadding="1" border="0" cellspacing="0">
			<xsl:call-template name="headers"/>
			<xsl:for-each select="moversreport/down/file">
				<xsl:call-template name="resultrow"/>
			</xsl:for-each>
		</table>
	</xsl:template>
	<xsl:template name="headers">
		<tr align="center" bgcolor="#8C66CC">
			<td>
				<font color="white">
					<b>File Name</b>
				</font>
			</td>
			<td>
				<font color="white">
					<b>Type</b>
				</font>
			</td>
			<td width="200">
				<font color="white">
					<b>PreviousRun</b>
				</font>
			</td>
			<td>
				<font color="white">
					<b>Previous Value</b>
				</font>
			</td>
			<td width="400">
				<font color="white">
					<b>TheCurrentRun</b>
				</font>
			</td>
			<td>
				<font color="white">
					<b>Current Value</b>
				</font>
			</td>
			<td>
				<font color="white">
					<b>Diff</b>
				</font>
			</td>
		</tr>
	</xsl:template>
	<xsl:template name="resultrow">
<!--		<xsl:variable name="cvslink">
			<xsl:apply-templates mode="cvsLink" select="@name"/>
		</xsl:variable>
		-->
		<xsl:variable name="fileurl" select="translate(@name, '\', '/')"/>
		<xsl:variable name="srcfile" select="substring-before($fileurl,'.java')"/>
		<xsl:variable name="_filelink" select="translate($fileurl, '/', '_')"/>
		<xsl:variable name="filelink" select="translate($_filelink, '.', '_')"/>
		<xsl:variable name="alternateColor">
			<xsl:if test="position() mod 2 = 1">#FFFFFF</xsl:if>
			<xsl:if test="position() mod 2 = 0">#E4CDFC</xsl:if>
		</xsl:variable>
		<xsl:for-each select="diff">
			<tr valign="middle">
				<xsl:attribute name="bgcolor"><xsl:value-of select="$alternateColor"/></xsl:attribute>
				<td>
					<xsl:if test="position() = 1">
						<a>
							<xsl:attribute name="href"><xsl:value-of select="concat('../xref/' , $srcfile, '.html')"/></xsl:attribute>
							<xsl:value-of select="$fileurl"/>
						</a>
<!--						<a>
							<xsl:attribute name="href"><xsl:value-of select="$cvslink"/></xsl:attribute>
								(ViewCVS)
							</a>-->
					</xsl:if>
					<xsl:if test="position() != 1">
						<xsl:text>&#160;</xsl:text>
					</xsl:if>
				</td>
				<td align="center">
					<xsl:if test="@type='checkstyle'">
						<a>
							<xsl:variable name="_temp0" select="concat('..',$module)"/>
							<xsl:variable name="_temp1" select="concat($_temp0,'/checkstyle-report.html#')"/>
							<xsl:attribute name="href"><xsl:value-of select="concat($_temp1 , $filelink)"/></xsl:attribute>
					checkstyle
				</a>
					</xsl:if>
					<xsl:if test="@type='pmd'">
						<a>
							<xsl:variable name="_temp0" select="concat('..',$module)"/>
							<xsl:variable name="_temp1" select="concat($_temp0,'/pmd-report.html#')"/>
							<xsl:attribute name="href"><xsl:value-of select="concat($_temp1 , $filelink)"/></xsl:attribute>
							<!--<xsl:variable name="_temp2" select="translate($srcfile, '/', '.')"/>
							<xsl:attribute name="href"><xsl:value-of select="$_temp1"/><xsl:value-of select="substring-before($_temp2, '.java')"/></xsl:attribute>-->
					pmd
				</a>
					</xsl:if>
					<xsl:if test="@type='findbugs'">findbugs
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
