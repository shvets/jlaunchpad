<!-- 
////////////////////////////////////////////////////////////////////////////////
//
//                  ObjectLab is sponsoring QALab
// 
// Based in London, we are world leaders in the design and development 
// of bespoke applications for the Securities Financing markets.
// 
// <a href="http://www.objectlab.co.uk/open">Click here to learn more</a>
//           ___  _     _           _   _          _
//          / _ \| |__ (_) ___  ___| |_| |    __ _| |__
//         | | | | '_ \| |/ _ \/ __| __| |   / _` | '_ \
//         | |_| | |_) | |  __/ (__| |_| |__| (_| | |_) |
//          \___/|_.__// |\___|\___|\__|_____\__,_|_.__/
//                   |__/
//
//                   http://www.ObjectLab.co.uk
//
//QALab is released under the GNU General Public License.
//
//QALab: Collects QA Statistics from your build over time.
//2005+, ObjectLab Ltd
//
//This library is free software; you can redistribute it and/or
//modify it under the terms of the GNU General Public
//License as published by the Free Software Foundation; either
//version 2.1 of the License, or (at your option) any later version.
//
//This library is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
//General Public License for more details.
//
//You should have received a copy of the GNU General Public
//License along with this library; if not, write to the Free Software
//Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
////////////////////////////////////////////////////////////////////////////////
 -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:lxslt="http://xml.apache.org/xslt" xmlns:redirect="http://xml.apache.org/xalan/redirect" extension-element-prefixes="redirect">
	<xsl:output method="html" indent="yes" encoding="US-ASCII"/>
	<xsl:param name="targetdir"/>
	<xsl:param name="type"/>
	<xsl:param name="offset"/>
	<xsl:template match="qalab">
		<xsl:variable name="afile" select="concat($targetdir,'/index.html')"/>
		<!-- create the index.html -->
		<redirect:write file="{$afile}">
			<xsl:call-template name="index.html"/>
		</redirect:write>
		<redirect:write file="{$targetdir}/stylesheet.css">
			<xsl:call-template name="stylesheet.css"/>
		</redirect:write>
		<redirect:write file="{$targetdir}/all-packages.html">
			<xsl:call-template name="Classes"/>
		</redirect:write>
		<xsl:call-template name="Files"/>
		<redirect:write file="{$targetdir}/overview-summary.html">
			<xsl:call-template name="Main-Summary"/>
		</redirect:write>
	</xsl:template>
	<xsl:template name="index.html">
		<html>
			<head>
				<title>Historical <xsl:value-of select="$type"/> Statistics</title>
			</head>
			<frameset cols="30%,80%">
				<frame src="all-packages.html" name="packageListFrame"/>
				<frame src="overview-summary.html" name="classFrame"/>
			</frameset>
		</html>
	</xsl:template>
	<xsl:template name="stylesheet.css">
		<style type="text/css">
       .bannercell {
         border: 0px;
         padding: 0px;
       }
       body {
         margin-left: 10;
         margin-right: 10;
         font:normal 80% arial,helvetica,sanserif;
         background-color:#FFFFFF;
         color:#000000;
       }
       .a td { 
         background: #efefef;
       }
       .b td { 
         background: #fff;
       }
       th, td {
         text-align: left;
         vertical-align: top;
       }
       th {
         font-weight:bold;
         background: #ccc;
         color: black;
       }
       table, th, td {
         font-size:8pt;
         border: none
       }
       table.log tr td, tr th {
         
       }
       h2 {
         font-weight:bold;
         font-size:140%;
         margin-bottom: 5;
       }
       h3 {
         font-size:8pt;
         font-weight:bold;
         background: #525D76;
         color: white;
         text-decoration: none;
         padding: 5px;
         margin-right: 2px;
         margin-left: 2px;
         margin-bottom: 0;
       }
       
	</style>
	</xsl:template>
	<xsl:template name="Files">
		<xsl:for-each select="file">
			<xsl:variable name="jpgfile" select="translate(@id, '/', '_')"/>
			<xsl:variable name="redirectfile" select="translate(@id, '/', '_')"/>
			<xsl:if test="count(result[contains($type,@type)])>0">
				<xsl:variable name="afile" select="concat($redirectfile,'.html')"/>
				<redirect:write file="{$targetdir}/{$afile}">
					<html>
						<head>
							<link rel="stylesheet" type="text/css" href="stylesheet.css"/>
							<title><xsl:value-of select="$afile"/></title>
						</head>
						<body>
							<table width="100%">
								<tr align="left">
									<td>
        </td>
									<td align="right">
        [<a href="overview-summary.html">summary</a>]
        
   </td>
								</tr>
							</table>
							<h3>
								<a>
									<xsl:attribute name="name">PK<xsl:value-of select="@id"/></xsl:attribute>
									<xsl:variable name="srcfile" select="substring-before(@path,'.java')"/>
									<xsl:attribute name="href"><xsl:value-of select="concat('../xref/' , $srcfile, '.html')"/></xsl:attribute>
									<font color="white">
										<xsl:value-of select="@path"/>
									</font>
								</a>
							</h3>
							<a>
								<xsl:attribute name="href"><xsl:value-of select="concat($jpgfile, '.png')"/></xsl:attribute>
								<xsl:attribute name="target">chart</xsl:attribute>
								<img>
									<xsl:attribute name="src"><xsl:value-of select="concat($jpgfile, '.png')"/></xsl:attribute>
								</img>
							</a>
							<table class="log" border="0" cellpadding="1" cellspacing="1" width="50%">
								<tr>
									<th>Date</th>
									<th>Type</th>
									<th>Errors</th>
								</tr>
								<xsl:for-each select="result[contains($type,@type)]">
									<xsl:sort select="@date" order="descending"/>
									<tr>
										<xsl:call-template name="alternated-row"/>
										<td>
											<xsl:value-of select="@date"/>
										</td>
										<td>
											<xsl:value-of select="@type"/>
										</td>
										<td>
											<xsl:value-of select="@statvalue"/>
										</td>
									</tr>
								</xsl:for-each>
							</table>
						</body>
					</html>
				</redirect:write>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="Classes">
		<html>
			<head>
				<link rel="stylesheet" type="text/css" href="stylesheet.css"/>
			</head>
			<body>
				<table width="100%">
					<tr align="left">
						<td/>
						<td nowrap="nowrap" align="left">
							<a name="top">[<a href="../index.html" target="_top">Home</a>]</a>
  [<a href="overview-summary.html" target="classFrame">summary</a>]
  [<a href="#current">current</a>]
  [<a href="#past">past</a>]
   </td>
					</tr>
				</table>
				<a name="current">
					<h2>Current Files</h2>
				</a>
				<table width="100%">
					<xsl:for-each select="file">
						<xsl:sort select="@id"/>
						<xsl:if test="count(result[contains($type,@type)])>0">
							<xsl:call-template name="ListofClassesCurrent"/>
						</xsl:if>
					</xsl:for-each>
				</table>
				<br/>
				<br/>
				<a name="past">
					<h2>Past Files</h2>
				</a>
				[<a href="#top">top</a>]
				<table width="100%">
					<xsl:for-each select="file">
						<xsl:sort select="@id"/>
						<xsl:if test="count(result[contains($type,@type)])>0">
							<xsl:call-template name="ListofClassesPast"/>
						</xsl:if>
					</xsl:for-each>
				</table>
			</body>
		</html>
	</xsl:template>
	<xsl:template name="ListofClassesCurrent">
		<xsl:variable name="date" select="substring(result[last()]/@date,1,10)"/>
		<xsl:variable name="redirectfile" select="translate(@id, '/', '_')"/>
		<xsl:if test="translate($date,'-','') &gt; translate($offset,'-','')">
			<tr>
				<td nowrap="nowrap">
					<a target="classFrame">
						<xsl:attribute name="href"><xsl:value-of select="concat($redirectfile,'.html')"/></xsl:attribute>
						<xsl:value-of select="@path"/>
					</a>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<xsl:template name="ListofClassesPast">
		<xsl:variable name="date" select="substring(result[last()]/@date,1,10)"/>
		<xsl:variable name="redirectfile" select="translate(@id, '/', '_')"/>
		<!--<xsl:value-of select="translate($date,'-','')"/>-->
<!-- 		<xsl:if test="translate($date,'-','') &lt; translate($offset,'-','')">
			<tr>
				<td nowrap="nowrap">
					<a target="classFrame">
						<xsl:attribute name="href"><xsl:value-of select="concat($redirectfile,'.html')"/></xsl:attribute>
						<xsl:value-of select="@path"/>
					</a>
				</td>
			</tr>
		</xsl:if>-->
		<xsl:choose>
 	<xsl:when test="translate($date,'-','') &lt; translate($offset,'-','')"> </xsl:when>
		<xsl:otherwise>
			<tr>
				<td nowrap="nowrap">
					<a target="classFrame">
						<xsl:attribute name="href"><xsl:value-of select="concat($redirectfile,'.html')"/></xsl:attribute>
						<xsl:value-of select="@path"/>
					</a>
				</td>
			</tr>
		</xsl:otherwise>		
		</xsl:choose>
	</xsl:template>
	<xsl:template name="Main-Summary">
		<html>
			<head>
				<link rel="stylesheet" type="text/css" href="stylesheet.css"/>
			</head>
			<body>
				<h3>Summary</h3>
				<a href="summary.png" target="chart">
					<img src="summary.png"/>
				</a>
				<table class="log" border="0" cellpadding="5" cellspacing="2" width="100%">
					<tr>
						<th>Date</th>
						<th>Type</th>
						<th>Files</th>
						<th>Errors</th>
					</tr>
					<xsl:for-each select="summary/summaryresult[contains($type,@type)]">
						<xsl:sort select="@date" order="descending"/>
						<tr>
							<xsl:call-template name="alternated-row"/>
							<td>
								<xsl:value-of select="@date"/>
							</td>
							<td>
								<xsl:value-of select="@type"/>
							</td>
							<td>
								<xsl:value-of select="@filecount"/>
							</td>
							<td>
								<xsl:value-of select="@statvalue"/>
							</td>
						</tr>
					</xsl:for-each>
				</table>
				<hr size="1" width="100%" align="left"/>
			</body>
		</html>
	</xsl:template>
	<xsl:template name="alternated-row">
		<xsl:attribute name="class"><xsl:if test="position() mod 2 = 1">a</xsl:if><xsl:if test="position() mod 2 = 0">b</xsl:if></xsl:attribute>
	</xsl:template>
</xsl:stylesheet>
