<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
	xmlns:x2="http://www.decision-deck.org/2009/XMCDA-2.1.0"
	xmlns:x3="http://www.decision-deck.org/xmcda3"
	xmlns:uta="http://www.decision-deck.org/xmcda3/uta">
	
	<xsl:output method="xml" indent="yes" omit-xml-declaration="no" encoding="UTF-8"/>

	<xsl:template match="x2:XMCDA">
		<uta:utagmsInput xsi:schemaLocation="http://www.decision-deck.org/xmcda3/uta utagms-input.xsd">
			<xsl:apply-templates select="alternatives" />
			<xsl:apply-templates select="alternativesCriteriaValues" />			
			<xsl:apply-templates select="alternativesComparisons/pairs" />
		</uta:utagmsInput>
	</xsl:template>
	
	<xsl:template match="alternatives">
		<alternatives>
			<xsl:apply-templates select="alternative" />
		</alternatives>
	</xsl:template>
	
	<xsl:template match="alternative">
		<alternative>
			<id><xsl:value-of select="@id" /></id>
		</alternative>
	</xsl:template>

	<xsl:template match="alternativesCriteriaValues">
		<performances>
			<xsl:apply-templates select="alternativeCriteriaValue" />
		</performances>
	</xsl:template>
	
	<xsl:template match="alternativeCriteriaValue">
		<valuedPair>
			<from ref="{alternativeID}" />
			<to ref="{criterionValue/criterionID}" />
			<measurement>
				<value><xsl:value-of select="criterionValue/value/real" /></value>
			</measurement>
		</valuedPair>
	</xsl:template>
	
	<xsl:template match="pairs">
		<preferences>
			<xsl:apply-templates select="pair" />
		</preferences>
	</xsl:template>

	<xsl:template match="pair">
		<valuedPair>
			<xsl:apply-templates select="initial|terminal" />
		</valuedPair>
	</xsl:template>

	<xsl:template match="initial">
		<from ref="{alternativeID}" />
	</xsl:template>
	<xsl:template match="terminal">
		<to ref="{alternativeID}" />
	</xsl:template>
	
</xsl:stylesheet>
