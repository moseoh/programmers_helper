<#if dto.helpComment?has_content>
${dto.helpComment}
</#if>
<#if dto.useMain>
def print_result(index, result, answer):
    correct = result == answer
    if correct:
        print(f"\n테스트 케이스 {index}: 정답")
        print(f"\t- 실행 결과: {result}")
        print(f"\t- 기댓값: {answer}")
    else:
        raise Exception(f"\n테스트 케이스 {index}: 오답\n\t- 실행 결과: {result}\n\t- 기댓값: {answer}")


if __name__ == "__main__":
<#list dto.testCaseDtos as testCase>
    <#assign index = testCase?counter>
    # 테스트 케이스 ${index}
    <#list testCase.values as value>
    ${value.name}_${index} = ${value.value}
    </#list>
    answer_${index} = ${testCase.answer.value}
    result_${index} = solution(<#list testCase.values as value>${value.name}_${index}<#if value_has_next>, </#if></#list>)
    print_result(${index}, result_${index}, answer_${index})<#if testCase_has_next>

</#if>
</#list>



</#if>
def ${dto.functionName}(<#list dto.testCaseDtos[0].values as value>${value.name}<#if value_has_next>, </#if></#list>):
    ${dto.functionContent}