questions := 'input' asFileReference contents lines.

result := questions inject: 0 into:[:sum :each |
	(CompilationContext evaluate: each) asInteger + sum].
