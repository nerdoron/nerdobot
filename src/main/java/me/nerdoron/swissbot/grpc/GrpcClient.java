package me.nerdoron.swissbot.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GrpcClient {
	
	public static void grpc() {
		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 3020).usePlaintext().build();
		
		
	}

}
