# 编译原理课程实验源代码  
## 实验一：词法分析  
👉 [LexicalAnalyzer.java](https://github.com/MonreeStudio/CompilersilersPrinciples_CourseExperiment/blob/master/LexicalAnalyzer.java)  
### 任务 
对指导书所给程序进行词法分析，输出单词的种别编码和值。（此处仅展示源代码）  
### 备注  
代码仅针对实验指导书所给的程序进行编写和调试，未进行严格的鲁棒性测试。  
## 实验二：语法分析(算术表达式的扩充)  
👉 [GrammarAnalyzer.java](https://github.com/MonreeStudio/CompilersilersPrinciples_CourseExperiment/blob/master/GrammarAnalyzer.java)
### 任务  
根据实验指导书所给文法重新设计LR分析表，并修改语义加工程序，最后验证修改的结果。  
### 备注 
终于搞定了语法分析。😆  
使用了LR(0)分析方法，分析的文法是：S -> BB, B -> aB|b  
因为LR(0)分析表是手动输入的，所以只能针对以上文法进行分析。
## 实验三：设计一个完整的编译程序
### 任务 
按照编译原理的理论，实现一个编译器。  
### 备注
老师说这是一个小组合作实验，不知难度几何。 (￣▽￣)"
