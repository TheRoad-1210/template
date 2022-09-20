package cn.edu.hitsz.compiler.lexer;

import cn.edu.hitsz.compiler.symtab.SymbolTable;
import cn.edu.hitsz.compiler.utils.FileUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

/**
 * TODO: 实验一: 实现词法分析
 * <br>
 * 你可能需要参考的框架代码如下:
 *
 * @see Token 词法单元的实现
 * @see TokenKind 词法单元类型的实现
 */
public class LexicalAnalyzer {
    private final SymbolTable symbolTable;
    private List<String> txt;
    private StringBuilder stringBuilder = new StringBuilder();
    private char ch;
    private HashMap<TokenKind,String> codeList = new HashMap<TokenKind, String>();

    public LexicalAnalyzer(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    private char[] digit = {'0','1','2','3','4','5','6','7','8','9'};
    private char[] letter = {'a','b','c','d','e','f','g','h','i','j',
    'k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'
    };

    /**
     * 从给予的路径中读取并加载文件内容
     *
     * @param path 路径
     */
    public void loadFile(String path) {
        txt = FileUtils.readLines(path);

        //  词法分析前的缓冲区实现
        // 可自由实现各类缓冲区
        // 或直接采用完整读入方法
    }

    /**
     * 执行词法分析, 准备好用于返回的 token 列表 <br>
     * 需要维护实验一所需的符号表条目, 而得在语法分析中才能确定的符号表条目的成员可以先设置为 null
     */
    public void run() {
        for (String s: txt){
            if (stringBuilder.length() != 0){
                stringBuilder.delete(0,stringBuilder.length());
            }
            for (int i = 0; i < s.length(); i++) {
                ch = s.charAt(i);
                if(ch == ' ' && stringBuilder.length() != 0){
                    stringBuilder.delete(0,stringBuilder.length());
                }
                if(isLetter()){
                    while (isLetter()||isDigit()){
                        stringBuilder.append(ch);
                        i++;
                        ch = s.charAt(i);
                    }
                    codeList.put(TokenKind.fromString("id"),stringBuilder.toString());
                }

            }

        }
        // TODO: 自动机实现的词法分析过程
//        throw new NotImplementedException();
    }

    /**
     * 获得词法分析的结果, 保证在调用了 run 方法之后调用
     *
     * @return Token 列表
     */
    public Iterable<Token> getTokens() {
        LinkedList<Token> tokens = new LinkedList<>();
        for (Map.Entry<TokenKind, String> entry :codeList.entrySet()){
            if(entry.getValue() != null){
                tokens.add(Token.normal(entry.getKey(),entry.getValue()));
            }else {
                tokens.add(Token.simple(entry.getKey()));
            }
        }
        return tokens;
    }

    public void dumpTokens(String path) {
        FileUtils.writeLines(
            path,
            StreamSupport.stream(getTokens().spliterator(), false).map(Token::toString).toList()
        );
    }

    private boolean isDigit(){
        for (char c: digit){
            if(ch == c){
                return true;
            }
        }
        return false;
    }

    private  boolean isLetter(){
        for (char c: letter){
            if(ch == c){
                return true;
            }
        }
        return false;
    }

}
