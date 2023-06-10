import {
  Button,
  Card,
  Modal,
  Space,
  Table,
  Tag,
  Timeline,
  Typography,
  Empty,
  Statistic,
} from "antd";
import dayjs from "dayjs";
import { useEffect, useState } from "react";

const BlockchainGraph = ({ blockchain }) => {
  return (
    <div
      style={{
        display: "flex",
        flexDirection: "column",
        width: "calc(70% - 48px)",
        height: "100%",
      }}
    >
      <Typography.Title
        level={3}
        style={{
          paddingLeft: 24,
          paddingTop: 24,
          paddingBottom: 24,
        }}
      >
        Blockchain
      </Typography.Title>
      <div
        style={{
          width: "100%",
          height: "100%",
          overflowY: "auto",
          overflowX: "hidden",
          padding: 24,
        }}
      >
        {blockchain && blockchain.length > 0 ? (
          <Timeline
            items={blockchain?.map((block) => {
              return {
                children: <BlockChainCard block={block} />,
              };
            })}
          />
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
            <Empty />
          </div>
        )}
      </div>
    </div>
  );
};

const BlockChainCard = ({ block }) => {
  const [openTransaction, setOpenTransaction] = useState(false);
  const [transaction, setTransaction] = useState({});

  return (
    <Card
      size="small"
      title={dayjs(block.timestamp).format("YYYY-MM-DD HH:mm:ss")}
    >
      <Space align="start" size={32} stlye={{ width: "100%" }}>
        <Space direction="vertical">
          <Space align="center">
            <Typography.Text strong>Hash:</Typography.Text>
            <Typography.Text>
              <Tag color="blue">{block.hash}</Tag>
            </Typography.Text>
          </Space>
          <Space align="center">
            <Typography.Text strong>Previous block hash:</Typography.Text>
            <Typography.Text>
              <Tag color={block.previousHash === "0" ? "error" : "success"}>
                {block.previousHash && block.previousHash !== "0"
                  ? block.previousHash
                  : "Genesis"}
              </Tag>
            </Typography.Text>
          </Space>
          <Space align="center">
            <Typography.Text strong>Number of transactions:</Typography.Text>
            <Typography.Text>
              <Tag>{block.data ? block.data.length : 0}</Tag>
            </Typography.Text>
          </Space>
          <Space align="center">
            <Typography.Text strong>
              Nonce (number of attempts):
            </Typography.Text>
            <Typography.Text>
              <Tag>{block.nonce ? block.nonce : "Genesis"}</Tag>
            </Typography.Text>
          </Space>
        </Space>
        <Space direction="vertical">
          {block.data && block.data.length > 0
            ? block.data.map((transaction) => (
                <Space align="center">
                  <Typography.Text strong>Transaction ID:</Typography.Text>
                  <Typography.Text>
                    <Tag color="blue">{transaction.id}</Tag>
                  </Typography.Text>
                  <Button
                    type="link"
                    onClick={() => {
                      setTransaction(transaction);
                      setOpenTransaction(true);
                    }}
                  >
                    Show Transaction
                  </Button>
                </Space>
              ))
            : "No transactions"}
        </Space>
      </Space>
      <Modal
        title="Transaction Details"
        open={openTransaction}
        onCancel={() => {
          setOpenTransaction(false);
          setTransaction({});
        }}
        centered
        footer={[]}
      >
        <Space direction="vertical" size={12}>
          <Space align="center">
            <Typography.Text type="secondary">Transaction ID:</Typography.Text>
            <Typography.Text>
              <Tag color="blue">{transaction.id}</Tag>
            </Typography.Text>
          </Space>
          <Space align="center">
            <Typography.Text type="secondary">Amount:</Typography.Text>
            <Typography.Text>
              <Tag>
                <Statistic
                  valueStyle={{
                    fontSize: 14,
                  }}
                  value={transaction.value}
                  precision={2}
                />
              </Tag>
            </Typography.Text>
          </Space>
          <Space align="center">
            <Typography.Text type="secondary">Sender:</Typography.Text>
            <Typography.Text>
              <Tag color="warning">{transaction.sender}</Tag>
            </Typography.Text>
          </Space>
          <Space align="center">
            <Typography.Text type="secondary">Recipient:</Typography.Text>
            <Typography.Text>
              <Tag color="success">{transaction.recipient}</Tag>
            </Typography.Text>
          </Space>
          <Space align="center">
            <Typography.Text type="secondary">Valid?</Typography.Text>
            <Typography.Text>
              <Tag color={transaction.valid ? "success" : "error"}>
                {transaction.valid ? "Yes" : "No"}
              </Tag>
            </Typography.Text>
          </Space>
        </Space>
      </Modal>
    </Card>
  );
};

export default BlockchainGraph;
