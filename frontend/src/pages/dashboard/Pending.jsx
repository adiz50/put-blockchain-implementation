import { Card, Empty, Space, Statistic, Tag, Typography } from "antd";

const PendingTransactionsColumn = ({ pendingTransactions }) => {
  return (
    <div
      style={{
        width: "calc(30% - 48px)",
        height: "100%",
        overflowY: "auto",
        overflowX: "hidden",
        padding: 24,
      }}
    >
      <Typography.Title level={3}>Pending transactions</Typography.Title>
      {pendingTransactions && pendingTransactions.length > 0 ? (
        pendingTransactions.map((transaction) => (
          <Card size="small">
            <Space direction="vertical">
              <Space align="center">
                <Typography.Text strong>Sender:</Typography.Text>
                <Tag color="warning">{transaction.sender}</Tag>
              </Space>
              <Space align="center">
                <Typography.Text strong>Recipient:</Typography.Text>
                <Tag color="success">{transaction.recipient}</Tag>
              </Space>
              <Space align="center">
                <Typography.Text strong>Amount:</Typography.Text>
                <Tag>
                  <Statistic
                    valueStyle={{
                      fontSize: 14,
                    }}
                    value={transaction.value}
                    precision={2}
                  />
                </Tag>
              </Space>
            </Space>
          </Card>
        ))
      ) : (
        <div
          style={{
            height: "85%",
            width: "100%",
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
          }}
        >
          <Empty description="No pending transactions" />
        </div>
      )}
    </div>
  );
};

export default PendingTransactionsColumn;
