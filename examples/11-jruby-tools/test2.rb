#

require "java"

# Include Java's String as JString
include_class("java.lang.String") { |package, name| "J" + name }

s = JString.new("Hello World from JRuby!")
puts s