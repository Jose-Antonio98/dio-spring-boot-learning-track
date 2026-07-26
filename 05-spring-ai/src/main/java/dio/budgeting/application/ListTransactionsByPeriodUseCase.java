package dio.budgeting.application;

import dio.budgeting.application.output.TransactionOutput;
import dio.budgeting.domain.TransactionRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ListTransactionsByPeriodUseCase {

    private final TransactionRepository transactionRepository;

    public ListTransactionsByPeriodUseCase(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Tool(
            name = "list-transactions-by-period",
            description = "Lista transações financeiras dentro de um período informado"
    )
    public List<TransactionOutput> execute(
            @ToolParam(description = "Data inicial do período")
            LocalDate startDate,

            @ToolParam(description = "Data final do período")
            LocalDate endDate
    ) {

        return transactionRepository
                .findAllByDateBetween(startDate, endDate)
                .stream()
                .map(TransactionOutput::from)
                .toList();
    }
}
