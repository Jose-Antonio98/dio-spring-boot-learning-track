package dio.budgeting.application;

import dio.budgeting.domain.Transaction;
import dio.budgeting.domain.TransactionRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class GetExpensesSummaryUseCase {

    private final TransactionRepository transactionRepository;

    public GetExpensesSummaryUseCase(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }


    @Tool(
            name = "get-expenses-summary",
            description = "Calcula um resumo financeiro dos gastos dentro de um período"
    )
    public String execute(
            @ToolParam(description = "Data inicial do período")
            LocalDate startDate,

            @ToolParam(description = "Data final do período")
            LocalDate endDate
    ) {

        List<Transaction> transactions =
                transactionRepository.findAllByDateBetween(
                        startDate,
                        endDate
                );


        if (transactions.isEmpty()) {
            return "Não foram encontradas transações nesse período.";
        }


        long totalAmount = transactions.stream()
                .mapToLong(Transaction::getAmount)
                .sum();


        long averageAmount = totalAmount / transactions.size();


        return String.format(
                """
                Resumo financeiro:

                Período: %s até %s

                Quantidade de transações: %d

                Total gasto: R$ %.2f

                Média por transação: R$ %.2f
                """,
                startDate,
                endDate,
                transactions.size(),
                totalAmount / 100.0,
                averageAmount / 100.0
        );
    }
}